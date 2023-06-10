package org.example.util;

import ch.qos.logback.core.util.FileUtil;
import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
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

    public void write(Object data, String destinationPath) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(destinationPath);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        Yaml yaml = new Yaml(options);
        yaml.dump(data, writer);
    }
}
