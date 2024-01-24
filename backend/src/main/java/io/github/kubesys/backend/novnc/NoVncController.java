package io.github.kubesys.backend.novnc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author bingshuai@nj.iscas.ac.cn
 * @since 11.01
 */

@Validated
@Controller
@Scope("prototype")
public class NoVncController {

    private String port;
    

    public NoVncController() {
		super();
	}

	public NoVncController(String port) {
		super();
		this.port = port;
	}

	@Value("${server.port:8081}")
    private void setPort(String value) {
        this.port = value;
    }

    public String getPort() {
		return port;
	}

	@GetMapping("/novnc/")
    public Object novnc(@RequestParam(required=true,name="port") String port,Model model) {
        String filePath = "templates/vnc.html";
        ClassPathResource resource = new ClassPathResource(filePath);
        if (resource.exists()) {
            try {
                model.addAttribute("port", port);
            }catch (Exception ignored) {}

            model.addAttribute("port", port);
            return "vnc";
        }
        return null;
    }

}
