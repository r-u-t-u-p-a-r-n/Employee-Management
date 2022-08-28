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
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_XML_VALUE)
public class AdminController
{
	@Autowired
    private AdminServices adminServices ;

    public AdminController(AdminServices adminServices)
    {
        this.adminServices = adminServices;
    }

    @GetMapping("/emp/{id}")
    public List<EmployeeData> getEmployeeData(@PathVariable("id") String id)
    {
    	return adminServices.getEmployeeData(id);
    }

    @GetMapping("/emp/{id}/{property}")
    public List<EmployeeData> getEmployeeData(@PathVariable("id") String id, @PathVariable("property") String property)
    {
    	return adminServices.getEmployeeData(id,property);
    }

    @PostMapping("/emp/add")
	public ResponseEntity<EmployeeData> AddNewEmployee(@RequestBody EmployeeData employeeData)
	{
		return adminServices.AddNewEmployee(employeeData);
	}

	@PutMapping("/emp/update/{id}")
	public ResponseEntity<EmployeeData> updateEmployeeData(@RequestBody EmployeeData employeeData,
														   @PathVariable("id") String id)
	{
		return adminServices.updateEmployeeData(employeeData,id);
	}

	@PostMapping("/ast/add")
	public ResponseEntity<AssetsData> AddNewAsset(@RequestBody AssetsData assetsData)
	{
		return adminServices.addNewAsset(assetsData);
	}

	@PutMapping("/ast/update/{id}")
	public ResponseEntity<AssetsData> updateAssetsData(@RequestBody AssetsData assetsData, 
													   @PathVariable("id") String id)
	{
		return adminServices.updateAssetsData(assetsData,id);
	}
}