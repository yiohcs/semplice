package com.yschoi.catalog.controller;

import com.yschoi.catalog.model.CatalogVo;
import com.yschoi.catalog.model.ServiceVo;
import com.yschoi.catalog.service.CatalogService;
import com.yschoi.login.model.TokenAndId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    private TokenAndId tokenAndId = new TokenAndId();

    @PostMapping("/api/catalog/setTokenAndId")
    public void setBearerToken(@RequestBody TokenAndId tokenAndId) {
        this.tokenAndId.setBearerToken(tokenAndId.getBearerToken());
        this.tokenAndId.setUserId(tokenAndId.getUserId());
        this.tokenAndId.setName(tokenAndId.getName());

        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getBearerToken());
        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getUserId());
    }

    @PostMapping("/api/catalog/createCatalog")
    public String createCatalog(@RequestBody CatalogVo catalogVo) {
        System.out.println("createCatalog >>> ");
        return catalogService.createCatalog(catalogVo,tokenAndId.getBearerToken());

    }

    @PostMapping("/api/catalog/addService")
    public String addService(@RequestBody ServiceVo serviceVo) {

        return catalogService.addService(serviceVo,tokenAndId.getBearerToken());
    }

    @PostMapping("/api/catalog/getServices")
    public String getServices(@RequestBody CatalogVo catalogVo) {

        System.out.println("getServices ------- ");
        return catalogService.getServices(catalogVo,tokenAndId.getBearerToken());

    }

    @PostMapping("/api/catalog/getApps")
    public String getApps(@RequestBody CatalogVo catalogVo) {

        System.out.println("getApps ------- ");
        return catalogService.getApps(catalogVo,tokenAndId.getBearerToken());

    }

}
