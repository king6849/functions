package com.king.functions.excel.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExcelPageController {
    private static final String page_base_path = "ui/page";

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public ModelAndView testPage() {
        return new ModelAndView(page_base_path + "/home");
    }

}
