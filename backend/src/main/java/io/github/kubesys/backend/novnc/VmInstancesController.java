package io.github.kubesys.backend.novnc;


import io.github.kubesys.backend.services.IVmInstancesService;

import io.github.kubesys.backend.utils.WebViews;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author bingshuai@nj.iscas.ac.cn
 * @since 11.01
 */

@Validated
@Controller("VmInstances")
@Scope("prototype")
@RequestMapping("VmInstance")
public class VmInstancesController {
    private final IVmInstancesService vminstancesService;


    public VmInstancesController(IVmInstancesService vminstancesService) {
		super();
		this.vminstancesService = vminstancesService;
	}


	@GetMapping("/viewNoVnc")
    public Object viewAddProduct( Model model) {
        try {
            Integer port = vminstancesService.startWebsockifyServer();
            model.addAttribute("port", port);
            return WebViews.view("/VmInstances/viewNoVnc");
        }catch (Exception e) {
            return e;
        }
    }

	public IVmInstancesService getVminstancesService() {
		return vminstancesService;
	}

}
