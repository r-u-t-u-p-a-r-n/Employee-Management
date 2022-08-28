package site.operations.Services;

import site.operations.Models.OrgData;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrgServices
{
	public ResponseEntity<OrgData> addNewOrg(OrgData orgData);
	public ResponseEntity<String> deleteOrgData();
	public ResponseEntity<OrgData> updateOrgData(OrgData orgData);
	public List<String> deleteEmployeeData(String id);
	public List<String> deleteAssetsData(String id);
}