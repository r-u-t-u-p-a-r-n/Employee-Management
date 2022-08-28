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
public class BasicImpl implements BasicServices,ExtraFunctions
{
	@Autowired
	private EmployeeRepo employeeRepo ;

	@Autowired
	private AssetsRepo assetsRepo ;

	@Autowired
	private BasicAuthRepository basicAuthRepository ;

	@Autowired
	private OrgRepo orgRepo ;

	public BasicImpl(BasicAuthRepository basicAuthRepository, OrgRepo orgRepo,
		             EmployeeRepo employeeRepo, AssetsRepo assetsRepo)
	{
		this.basicAuthRepository = basicAuthRepository ;
		this.orgRepo = orgRepo ;
		this.employeeRepo = employeeRepo ;
		this.assetsRepo = assetsRepo ;
	}

    @Override
	public OrgData getOrgData2()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<BasicAuth> L0 = basicAuthRepository.findAll();
		for(BasicAuth BA : L0)
		{
			if(BA.getEmail().equals(auth.getName()))
			{
				List<OrgData> L1 = orgRepo.findAll();
				for(OrgData OG : L1)
				{
					if(OG.getOrgId().equals(BA.getUserId()))
						return OG ;
				}
				List<EmployeeData> L2 = employeeRepo.findAll();
				for(EmployeeData ED : L2)
				{
					if(ED.getEmployeeId().equals(BA.getUserId()))
					{
						for(OrgData OG : L1)
						{
							if(OG.getOrgId().equals(ED.getOrganizationId()))
								return OG ;
						}
					}
				}
			}
		}

		return null ;
	}

	private final EmployeeData getEmployeeDetails()
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

    private final String getOrgId()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BasicAuth> L0 = basicAuthRepository.findAll();
        for(BasicAuth BA : L0)
        {
            if(BA.getEmail().equals(auth.getName()))
            {
                List<OrgData> L1 = orgRepo.findAll();
                for(OrgData OD : L1)
                {
                    if(OD.getOrgId().equals(BA.getUserId()))
                        return OD.getOrgId() ;
                }

                List<EmployeeData> L2 = employeeRepo.findAll();
                for(EmployeeData ED : L2)
                {
                    if(ED.getEmployeeId().equals(BA.getUserId()))
                        return ED.getOrganizationId() ;
                }
            }
        }

        return null ;
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
            BA.setPassword(passwordEncoder().encode(basicAuth.getPassword()));

        return basicAuthRepository.save(BA);
	}

	@Override
    public List<AssetsData> getAssetsData(String id)
    {
        Set<String> S = ExtraFunctions.getValues(id,',');
        List<AssetsData> L = new ArrayList<AssetsData> ();
        for(String str : S)
        {
        	if(str.equals("*"))
	        {
	        	List<AssetsData> L0 = assetsRepo.findAll();
	        	for(AssetsData A : L0)
	        	{
	        		if(A.getOrganizationId().equals(getOrgId()))
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
	            if(!(AD.getOrganizationId().equals(getOrgId())))
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
    	return getEmployeeDetails();
    }

    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}