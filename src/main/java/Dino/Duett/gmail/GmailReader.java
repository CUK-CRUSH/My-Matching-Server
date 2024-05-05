package Dino.Duett.gmail;

import Dino.Duett.config.EnvBean;
import jakarta.mail.*;
import jakarta.mail.search.FromStringTerm;
import jakarta.mail.search.FromTerm;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static Dino.Duett.global.ErrorMessage.*;

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

    private Message getLastMessages(Message[] messages) throws ResponseStatusException {
        if (messages == null || messages.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_MESSAGES_FOUND.getMessage());
        }
        return messages[messages.length - 1];
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
        try (Store store = SESSION.getStore()) {
            store.connect(envBean.getEmailUsername(), envBean.getEmailPassword());

            try (Folder inbox = store.getFolder("INBOX")) {
                inbox.open(Folder.READ_ONLY);

                // 발신자 번호를 이용한 검색
//                for (String domain : DOMAINS) {
//                }
                Message[] messages = inbox.search(new FromStringTerm(phoneNumber));
                // 가장 최근 메일을 가져옴
                Message lastMessage = getLastMessages(messages);
                System.out.print("lastMessage: ");
                System.out.println(Arrays.toString(lastMessage.getAllRecipients()));
                System.out.print("lastMessage: ");
                System.out.println(Arrays.toString(lastMessage.getFrom()));
                // 메일의 내용을 가져옴
                String body = getBody(lastMessage);
                // 메일 내용과 코드가 일치하는지 확인
                if (!body.equals(code)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_VALIDATION_FAILED.getMessage());
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
