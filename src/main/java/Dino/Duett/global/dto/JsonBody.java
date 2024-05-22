package Dino.Duett.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor(staticName = "of")
public class JsonBody<T> implements ResponseBody {
    @NotNull
    @Schema(example = "200")
    private final int status;
    @NotBlank
    @Schema(example = "성공")
    private final String message;
    private final T data;
}
