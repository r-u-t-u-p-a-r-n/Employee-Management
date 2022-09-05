package site.operations.Controller;

import site.operations.Models.*;
import site.operations.Services.BasicServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping(value = "/basic", produces = MediaType.APPLICATION_XML_VALUE)
public class BasicController
{
	@Autowired
    private BasicServices basicServices ;

    public BasicController(BasicServices basicServices)
    {
        this.basicServices = basicServices;
    }

    @GetMapping("/assets/{id}")
    public List<AssetsData> getAssetsData(@PathVariable("id") String id)
    {
    	return basicServices.getAssetsData(id);
    }

    @GetMapping("/org")
    public OrgData getOrgDetails()
    {
        return basicServices.getOrgData();
    }

    @GetMapping("/login")
    public BasicAuth getLoginInfo()
    {
        return basicServices.getLoginInfo();
    }

    @PutMapping("/update")
    public BasicAuth updateLoginInfo(@RequestBody BasicAuth basicAuth)
    {
        return basicServices.updateLoginInfo(basicAuth);
    }

    @GetMapping("/employee/profile")
    public EmployeeData getEmpDetails()
    {
        return basicServices.getEmployeeProfile();
    }
}