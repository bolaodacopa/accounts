package tk.bolaodacopa.accounts.security.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class ResourceUtil {

/*	
	@Autowired
	private ResourceLoader resourceLoader;


	public String asString(String resourcePath) throws IOException {
		Resource resource = resourceLoader.getResource(resourcePath);
		try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		}
	}	
*/

    public String asString(String resourcePath) throws IOException {



        //InputStream is = new ClassPathResource(resourcePath).getInputStream();
    	InputStream is = new FileInputStream(resourcePath);



        try (Reader reader = new InputStreamReader(is, UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }


}
