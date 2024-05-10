package Dino.Duett.gmail;

import Dino.Duett.config.EnvBean;
import jakarta.mail.*;
import jakarta.mail.search.FromStringTerm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Properties;

import static Dino.Duett.gmail.enums.Message.*;

@Slf4j
@Component
public class GmailReader {
    private final EnvBean envBean;
    private final Session SESSION;
    private final String[] DOMAINS = {"@ktfmms.magicn.com", "@mmsmail.uplus.co.kr", "@vmms.nate.com"};
    GmailReader(EnvBean envBean) {
        this.envBean = envBean;
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        SESSION = Session.getInstance(properties);
    }

    private Message getLastMessages(Message[] messages) throws ResponseStatusException, MessagingException {
        if (messages == null || messages.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_MESSAGES_FOUND.getMessage());
        }
        int idx = messages.length - 1;
        // 통신 3사의 도메인 중 하나가 발신자에 포함되어 있는 메일을 찾음
        while (idx >= 0) {
            Address[] from = messages[idx].getFrom();
            // 주소에서 도메인을 확인. 대부분 주소는 하나만 있음
            for (Address address : from) {
                String addressStr = address.toString();
                // 통신 3사의 도메인이 포함되어 있는 경우 해당 메일을 반환
                for (String domain : DOMAINS) {
                    if (addressStr.contains(domain)) {
                        return messages[idx];
                    }
                }
            }
            idx--;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_MESSAGES_FOUND.getMessage());
    }

    private String getBody(Message message) throws IOException, MessagingException {
        Object content = message.getContent();
        // 메일의 내용이 multipart 아닌 경우 예외
        if (!(content instanceof Multipart multipart)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CONTENT_TYPE.getMessage());
        }
        BodyPart bodyPart = multipart.getBodyPart(0);
        String body = bodyPart.getContent().toString();
        // 메일의 내용 중 첫 줄만 가져옴
        int idx = body.indexOf("\r\n");
        if (idx != -1) {
            return body.substring(0, idx);
        }
        return body;
    }

    public void validate(String phoneNumber, String code) throws ResponseStatusException {
        // 전화번호가 숫자로만 이루어져 있는지 확인
        if (phoneNumber.matches(".*[^0-9].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_VALIDATION_FAILED.getMessage());
        }
        try (Store store = SESSION.getStore()) {
            store.connect(envBean.getEmailUsername(), envBean.getEmailPassword());

            try (Folder inbox = store.getFolder("INBOX")) {
                inbox.open(Folder.READ_ONLY);

                // 발신자 번호를 이용한 검색
                Message[] messages = inbox.search(new FromStringTerm(phoneNumber));
                // 가장 최근 메일을 가져옴
                Message lastMessage = getLastMessages(messages);
                // 메일의 내용을 가져옴
                String body = getBody(lastMessage);
                // 메일 내용과 코드가 일치하는지 확인
                if (!body.equals(code)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, EMAIL_VALIDATION_FAILED.getMessage());
                }
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to read email");
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, EMAIL_VALIDATION_FAILED.getMessage());
        }
    }
}
