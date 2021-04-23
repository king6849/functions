package com.king.function.excel.Utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class PathUtil {

    @Value("${default.base_save_path}")
    private String base_save_path;
    @Value("${default.base_export_path}")
    private String base_export_path;

}
