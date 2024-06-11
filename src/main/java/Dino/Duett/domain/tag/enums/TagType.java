package Dino.Duett.domain.tag.enums;

import Dino.Duett.domain.tag.exception.TagException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum TagType {
    MUSIC("음악", Arrays.asList(
            "팝", "힙합", "댄스", "일렉트로닉", "라틴", "록",
            "R&B", "메탈", "블루스", "소울", "아프로",
            "애니메이션", "앰비언트", "얼터너티브", "연주곡", "인디",
            "재즈", "컨트리", "Kpop", "클래식", "Jpop",
            "포크", "클래식", "파티", "로파이", "딥하우스",
            "Emo", "Funk", "디스코", "캐리비안", "뉴에이지",
            "메탈코어", "누메탈", "레게톤", "트로피칼", "EDM",
            "트랜스", "트로트", "발라드", "브릿팝", "보사노바")),
    HOBBY("취미",Arrays.asList(
            "영화", "캠핑", "클럽", "바다가기", "콘서트",
            "드라이빙", "공연", "경기관람", "박물관", "봉사활동",
            "공원가기", "게임하기", "유튜브 시청", "TV 시청", "음악",
            "댄스", "악기 연주", "글쓰기", "요리", "독서",
            "뜨개질", "그림 그리기", "바느질", "자수", "농구",
            "골프", "복싱", "야구", "배구", "스키",
            "요가", "축구", "테니스", "등산", "배드민턴",
            "스케이트", "낚시", "탁구", "보드", "자전거",
            "조깅", "걷기", "헬스"));
    private final String type;
    private final List<String> names;

    public static String findByMusicTagType(final String code) {
        return MUSIC.getNames().stream()
                .filter(v -> v.equals(code))
                .findAny()
                .orElseThrow(TagException.TagTypeNotFoundException::new);
    }

    public static String findByHobbyTagType(final String code) {
        return HOBBY.getNames().stream()
                .filter(v -> v.equals(code))
                .findAny()
                .orElseThrow(TagException.TagTypeNotFoundException::new);
    }

    public static List<String> getAllMusicTagTypes() {
        return MUSIC.getNames();
    }


    public static List<String> getAllHobbyTagTypes() {
        return HOBBY.getNames();
    }
}
