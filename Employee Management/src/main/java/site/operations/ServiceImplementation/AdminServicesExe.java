package site.operations.Implementation;

import site.operations.Models.*;
import site.operations.Repositories.*;
import site.operations.Services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

@Service
public class AdminServicesExe implements AdminServices
{
	@Autowired
	private EmployeeRepo employeeRepo ;

	@Autowired
	private AssetsRepo assetsRepo ;

	@Autowired
	private BasicAuthRepository basicAuthRepository ;

	@Autowired
	private OrgRepo orgRepo ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private ExtraFunctions EF ;

	@Override
    public ResponseEntity<EmployeeData> AddNewEmployee(EmployeeData employeeData)
    {
    	boolean b = true ;
    	String H = employeeData.getEmployeeId();
    	if(!(EF.checkId(H)))
    	{
    		employeeData.setEmployeeId(null);
    		b = false ;
    	}

        employeeData.setOrganizationDetails(EF.getOrgData());
        employeeData.setRole("EMPLOYEE");
        employeeData = EF.checkEmployeeValidity(employeeData);
        if(b && !(EF.checkId(employeeData.getEmployeeId())))
        {
        	BasicAuth BA = new BasicAuth();
	        BA.setUserId(employeeData.getEmployeeId());
	        BA.setEmail(employeeData.getEmployeeId()+"@myapi");
	        BA.setPassword(passwordEncoder.encode(employeeData.getEmployeeId()));
	        BA.setRole("EMPLOYEE");
        	basicAuthRepository.save(BA);
            return new ResponseEntity<EmployeeData> (employeeData,HttpStatus.CREATED);
        }
        if(H!=null && H.length()!=0)
        {
        	if(!(EF.checkId(H)))
        		employeeData.setEmployeeId("Id \'"+H+"\' already exists");
        	else
        		employeeData.setEmployeeId(H);
        }
        
        return new ResponseEntity<EmployeeData> (employeeData,HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<EmployeeData> getEmployeeData(String id)
    {
        List<EmployeeData> L = new ArrayList<EmployeeData> ();
        Set<String> S = EF.getValues(id,',');
        int i = 0 ;
        for(String str : S)
        {
            if(str.equals("*"))
            {
                List<EmployeeData> L0 = employeeRepo.findAll();
                for(EmployeeData E : L0)
                {
                    if(E.getOrganizationDetails().equals(EF.getOrgData()))
                        L.add(E);
                    i++ ;
                }
                return L ;
            }
        }
        EmployeeData ED ;
        for(String str : S)
        {
            try
            {
                ED = employeeRepo.findById(str).orElseThrow(Exception::new);
                if(!(ED.getOrganizationDetails().equals(EF.getOrgData())))
                    throw new Exception();
            }

            catch(Exception e)
            {
                ED = new EmployeeData();
                ED.setEmployeeId("Id \'"+str+"\' doesn\'t exist");
            }

            L.add(ED);
        }

        return L ;
    }

    @Override
    public List<EmployeeData> getEmployeeData(String id, String property)
    {
        Set<String> idvalues = EF.getValues(id,','),
        propertyValues = EF.getValues(property,',') ;
        EmployeeData Emp = new EmployeeData();
        List<EmployeeData> L = new ArrayList<EmployeeData> ();
        for(String str : idvalues)
        {
            for(String pv : propertyValues)
            {
                if(str.equals("*"))
                {
                    List<EmployeeData> L0 = employeeRepo.findAll();
                    if(property == null || pv.equals("*"))
                    {
                        for(EmployeeData E : L0)
			        	{
			        		if(E.getOrganizationDetails().equals(EF.getOrgData()))
			        			L.add(E);
			        	}
                    }
                    else
                    {
                    	L0 = EF.getPropertyData(L0,property) ;
					    for(EmployeeData E : L0)
						{
							if(E.getOrganizationDetails().equals(EF.getOrgData()))
                                L.add(E);
						}
                    }
                    return L ;
                }
                else if(property == null || pv.equals("*"))
                    return getEmployeeData(id);
            }
        }

        return EF.getPropertyData(getEmployeeData(id),property) ;
    }

    @Override
    public ResponseEntity<EmployeeData> updateEmployeeData(EmployeeData employeeData, String id)
    {
        EmployeeData ED ;
        try
        {
            ED = employeeRepo.findById(id).orElseThrow(Exception::new);
            if(employeeData.getEmployeeId()==null)
                employeeData.setEmployeeId(ED.getEmployeeId());

            if(!(ED.getOrganizationDetails().equals(EF.getOrgData())))
            	throw new Exception();

            if(employeeData.getName()==null)
                employeeData.setName(ED.getName());
            
            if(!(employeeData.getEmployeeId().equals(ED.getEmployeeId())))
                employeeData.setEmployeeId(ED.getEmployeeId());
            
            if(employeeData.getDepartment()==null)
                employeeData.setDepartment(ED.getDepartment());
            
            if(employeeData.getPosition()==null)
                employeeData.setPosition(ED.getPosition());
            
            if(employeeData.getAge()==null)
                employeeData.setAge(ED.getAge());
            
            if(employeeData.getJoined()==null)
                employeeData.setJoined(ED.getJoined());
            
            if(employeeData.getSalary()==null)
                employeeData.setSalary(ED.getSalary());

            if(employeeData.getStatus()==null)
                employeeData.setStatus(ED.getStatus());

            if(employeeData.getRole()==null)
                employeeData.setRole(ED.getRole());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            List<BasicAuth> L0 = basicAuthRepository.findAll();
            for(BasicAuth BA : L0)
            {
                if(auth.getName().equals(BA.getEmail()))
                {  
                  if(BA.getRole().equals("ORG"))
                    {
                        if(EF.checkCase(employeeData.getRole(),"ADMIN"))
                            ED.setRole("ADMIN");
                        if(EF.checkCase(employeeData.getRole(),"EMPLOYEE"))
                            ED.setRole("EMPLOYEE");

                        BasicAuth B = basicAuthRepository.findById(ED.getEmployeeId()).orElseThrow(Exception::new);
                        B.setRole(ED.getRole());
                        basicAuthRepository.save(B);
                    }
                    else
                        employeeData.setSalary(ED.getSalary());
                    break ;
                }
            }

            employeeData.setRole(ED.getRole());
            employeeData.setOrganizationDetails(ED.getOrganizationDetails());
            employeeData = EF.checkEmployeeValidity(employeeData);
            return new ResponseEntity<EmployeeData>(employeeData,HttpStatus.OK);
        }

        catch(Exception e)
        {
            employeeData.setEmployeeId("Id \'"+id+"\' not found");
            return new ResponseEntity<EmployeeData>(employeeData,HttpStatus.NOT_FOUND);
        }
    }

/*--------------------------------------------------------------------------------------------------*/

    @Override
    public ResponseEntity<AssetsData> addNewAsset(AssetsData assetsData)
    {
    	boolean b = true ;
    	String H = assetsData.getAssetId();
    	if(!(EF.checkId(H)))
    	{
    		assetsData.setAssetId(null);
    		b = false ;
    	}
        assetsData.setOrganizationDetails(EF.getOrgData());
        assetsData = EF.checkAssetsValidity(assetsData);
        if(b && !(EF.checkId(assetsData.getAssetId())))
        	return new ResponseEntity<AssetsData> (assetsData,HttpStatus.CREATED);
        if(H!=null && H.length()!=0)
        {
        	if(!(EF.checkId(H)))
        		assetsData.setAssetId("Id \'"+H+"\' already exists");
        	else
        		assetsData.setAssetId(H);
        }
        
        return new ResponseEntity<AssetsData> (assetsData,HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<AssetsData> updateAssetsData(AssetsData assetsData, String id)
    {
    	AssetsData A ;
    	try
    	{
    		A = assetsRepo.findById(id).orElseThrow(Exception::new);
    		
    		if(!(A.getOrganizationDetails().equals(EF.getOrgData())))
    			throw new Exception();

    		if(assetsData.getName() == null || assetsData.getName().length() == 0)
    			assetsData.setName(A.getName());

    		if(assetsData.getPrice() == null || assetsData.getPrice().length() == 0)
    			assetsData.setPrice(A.getPrice());

    		if(assetsData.getType() == null || assetsData.getType().length() == 0)
    			assetsData.setType(A.getType());

    		A.setName(assetsData.getName());
    		A.setType(assetsData.getType());
    		A.setPrice(assetsData.getPrice());
    		A = EF.checkAssetsValidity(A);
    		return new ResponseEntity<AssetsData> (A,HttpStatus.OK);
    	}

    	catch(Exception e)
    	{
    		A = new AssetsData();
    		A.setAssetId("Id \'"+id+"\' not found");
    		return new ResponseEntity<AssetsData> (A,HttpStatus.NOT_FOUND);
    	}
    }

/*--------------------------------------------------------------------------------------------------*/
}