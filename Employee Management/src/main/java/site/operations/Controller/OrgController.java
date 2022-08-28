package site.operations.Controller;

import site.operations.Models.*;
import site.operations.Services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping(value = "/org", produces = MediaType.APPLICATION_XML_VALUE)
public class OrgController
{
    @Autowired
    private OrgServices orgServices ;

    public OrgController(OrgServices orgServices)
    {
        this.orgServices = orgServices ;
    }

    @PostMapping("/add")
    public ResponseEntity<OrgData> addNewOrg(@RequestBody OrgData orgData)
    {
        return orgServices.addNewOrg(orgData);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteOrgData()
    {
        return orgServices.deleteOrgData();
    }

    @PutMapping("/update")
    public ResponseEntity<OrgData> updateOrgData(@RequestBody OrgData orgData)
    {
        return orgServices.updateOrgData(orgData);
    }

    @DeleteMapping("/emp/delete/{id}")
    public List<String> deleteEmployeeData(@PathVariable("id") String id)
    {
        return orgServices.deleteEmployeeData(id);
    }

    @DeleteMapping("ast/delete/{id}")
    public List<String> deleteAssetsData(@PathVariable("id") String id)
    {
        return orgServices.deleteAssetsData(id);
    }

/*--------------------------------------------------------------------------------------------------------------*/
}