package com.app.threetier.controller;

import com.app.threetier.domain.vo.ProductVO;
import com.app.threetier.service.ProductService;
import com.app.threetier.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products/*")
public class ProdcutController {
    private final ProductServiceImpl productService;

    @GetMapping("write")
    public void gotoWrite(ProductVO productVO) {;}

    @PostMapping("write-ok")
    public RedirectView writeOk(ProductVO productVO){
        productService.addProduct(productVO);
        return new RedirectView("/products/product?id="+productVO.getId());
    }

    @GetMapping("product")
    public void gotoProduct(Long id, Model model){
        ProductVO productVO = productService.getProduct(id);
        model.addAttribute("product", productVO);
    }
}
