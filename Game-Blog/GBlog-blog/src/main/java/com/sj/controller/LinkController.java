package com.sj.controller;

import com.sj.domain.ResponseResult;
import com.sj.service.LinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/*
 * The link page is for listing all user-submitted external links that might interest
 * other users after the website owner review the link.
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    @Resource
    private LinkService linkService;

    /**
     * Check for external links
     */
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}