package site.operations.Services;

import site.operations.Models.*;
import site.operations.Models.OrgData;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BasicServices
{
	public List<AssetsData> getAssetsData(String id);
	public OrgData getOrgData();
	public EmployeeData getEmployeeProfile();
	public BasicAuth getLoginInfo();
	public BasicAuth updateLoginInfo(BasicAuth basicAuth);
}