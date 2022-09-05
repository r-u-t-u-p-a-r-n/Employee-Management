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
import java.util.ArrayList;
import java.util.Set;

@Service
public class BasicImpl implements BasicServices
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
	private ExtraFunctions EF ;

	@Autowired
    private PasswordEncoder passwordEncoder ;
	
    @Override
    public OrgData getOrgData()
	{
		return EF.getOrgData();
	}

	@Override
	public BasicAuth getLoginInfo()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<BasicAuth> L = basicAuthRepository.findAll();
        for(BasicAuth B : L)
        {
            if(B.getEmail().equals(auth.getName()))
                return B ;
        }
        return null ;
	}
	
	@Override
	public BasicAuth updateLoginInfo(BasicAuth basicAuth)
	{
		BasicAuth BA = getLoginInfo();
            
        if(basicAuth.getPassword()==null || basicAuth.getPassword().length()==0)
            basicAuth.setPassword(BA.getPassword());
        else
            BA.setPassword(passwordEncoder.encode(basicAuth.getPassword()));

        return basicAuthRepository.save(BA);
	}

	@Override
    public List<AssetsData> getAssetsData(String id)
    {
        Set<String> S = EF.getValues(id,',');
        List<AssetsData> L = new ArrayList<AssetsData> ();
        for(String str : S)
        {
        	if(str.equals("*"))
	        {
	        	List<AssetsData> L0 = assetsRepo.findAll();
	        	for(AssetsData A : L0)
	        	{
	        		if(A.getOrganizationDetails().equals(EF.getOrgData()))
	        			L.add(A);
	        	}
	        	return L ;
	        }
        }

        AssetsData AD ;
        for(String str : S)
        {
	        try
	        {
	            AD = assetsRepo.findById(str).orElseThrow(Exception::new);
	            if(!(AD.getOrganizationDetails().equals(EF.getOrgData())))
	            	throw new Exception();
	        }

	        catch(Exception e)
	        {
	            AD = new AssetsData();
	            AD.setAssetId("No Asset found at \'"+str+"\' ");
	        }
	        L.add(AD);
	    }
	        
        return L ;
    }

    @Override
    public EmployeeData getEmployeeProfile()
    {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BasicAuth> L0 = basicAuthRepository.findAll();
        for(BasicAuth BA : L0)
        {
            if(BA.getEmail().equals(auth.getName()))
            {
                List<EmployeeData> L1 = employeeRepo.findAll();
                for(EmployeeData ED : L1)
                {
                    if(ED.getEmployeeId().equals(BA.getUserId()))
                        return ED ;
                }
            }
        }
        return null ;
    }
}