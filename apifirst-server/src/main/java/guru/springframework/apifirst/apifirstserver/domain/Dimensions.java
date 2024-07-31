package guru.springframework.apifirst.apifirstserver.domain;


import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Dimensions {
    private Integer height;
    private Integer width;
    private Integer length;
}
