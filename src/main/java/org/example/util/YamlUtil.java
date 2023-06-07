package org.example.util;

import ch.qos.logback.core.util.FileUtil;
import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@UtilityClass
public class YamlUtil {
    public Map<String, Object> read(String path) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream inputStream = FileUtil.class
                .getClassLoader()
                .getResourceAsStream(path);
        return yaml.load(inputStream);
    }
}
