package guru.springframework.apifirst.apifirstserver.domain;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Dimensions {

    @NotNull
    @Min(1)
    @Max(999)
    private Integer height;

    @NotNull
    @Min(1)
    @Max(999)
    private Integer width;

    @NotNull
    @Min(1)
    @Max(999)
    private Integer length;
}
