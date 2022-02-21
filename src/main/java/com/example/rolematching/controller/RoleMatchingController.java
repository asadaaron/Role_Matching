package com.example.rolematching.controller;
import com.example.rolematching.service.RoleMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;


@Controller
public class RoleMatchingController {
    @Autowired
    private RoleMatchingService roleMatchingService;

    @GetMapping(value = "/dashboard")
    //landing page when this end point will be triggered
    public String create () {

        return "tables";
    }

    /**
     *
     * @return menulist
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/tabledata", method = RequestMethod.GET)
    public String loadTableAndMenu () throws FileNotFoundException, UnsupportedEncodingException {
        String menuList = roleMatchingService.menuSample();
        return menuList;
    }

    /**
     *
     * @param contentSearch
     * @param percentage
     * @return menu list
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/searchrolematching/{contentId}/{percentage}", method = RequestMethod.GET)
    public String search(@PathVariable("contentId")String contentSearch ,@PathVariable("percentage") String percentage) throws FileNotFoundException, UnsupportedEncodingException {
        String menuList = roleMatchingService.searchContent(contentSearch,percentage);
        return menuList;
    }


    /**
     *
     * @param menuName
     * @return menulist
     */
    @ResponseBody
    @RequestMapping(value = "/tabledatafrorolematchin/{menuName}", method = RequestMethod.GET)
    public String tableDataForRoleMatching(@PathVariable("menuName") String menuName) {
        String menuList= null;
        try{
             menuList = roleMatchingService.toJsonString(menuName);
        }catch (Exception e){
            e.printStackTrace();
        }


        return menuList;
    }

    /**
     *
     * @param range
     * @return menu list based on the range
     */
    @ResponseBody
    @RequestMapping(value = "/rangewisemenu/{params}", method = RequestMethod.GET)
    public String rangeWiseRole(@PathVariable("params") String range) {
        String menuList = roleMatchingService.rangeWiseMenuList(range);
        return menuList;
    }

    /**
     *
     * @param sizeMax
     * @param sizeMin
     * @param scoreMin
     * @param scoreMax
     * @param isAll
     * @param searchString
     * @param propertyOrRole
     * @param percentageOfRole
     * @param atleastOne
     * @return menulist based on search criteria
     */
    @ResponseBody
    @RequestMapping(value = "/searchrolematching/{size_max}/{size_min}/{score_min}/{score_max}/{is_all}/{searchstring}/{property_or_role_id}/{percen_role}/{atleast_one}", method = RequestMethod.GET)
    public String advanceSearch(@PathVariable("size_max") String sizeMax,@PathVariable("size_min") String sizeMin,
                              @PathVariable("score_min") String scoreMin,@PathVariable("score_max") String scoreMax,
                              @PathVariable("is_all") String isAll,@PathVariable("searchstring") String searchString,
                              @PathVariable("property_or_role_id") String propertyOrRole,@PathVariable("percen_role") String percentageOfRole,
                              @PathVariable("atleast_one") String atleastOne) {
        String menuList = null;
        try {
            menuList = roleMatchingService.searchVariable(sizeMax,sizeMin,scoreMin,scoreMax,isAll,searchString,propertyOrRole,percentageOfRole,atleastOne);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //String menuList = greetingService.searchVariable(searchstring);
        return menuList;

    }

    /**
     *
     * @param all_mismatch
     * @return menulist based on mismatch on the role
     */
    @ResponseBody
    @RequestMapping(value = "/searchmismatchrolematching/{all_mismatch}", method = RequestMethod.GET)
    public String mismatchSearch(@PathVariable("all_mismatch") boolean all_mismatch) {
        String menuList = null;
        try {
            menuList = roleMatchingService.findMismatchRoleMatchigString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String menuList = greetingService.searchVariable(searchstring);
        return menuList;

    }

}