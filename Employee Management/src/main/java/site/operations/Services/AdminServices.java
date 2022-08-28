package site.operations.Services;

import site.operations.Models.EmployeeData;
import site.operations.Models.AssetsData;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminServices
{
	public List<EmployeeData> getEmployeeData(String id);
	public List<EmployeeData> getEmployeeData(String id, String property);
	public ResponseEntity<EmployeeData> AddNewEmployee(EmployeeData employeeData);
	public ResponseEntity<EmployeeData> updateEmployeeData(EmployeeData employeeData, String id);

    public ResponseEntity<AssetsData> addNewAsset(AssetsData assetsData);
    public ResponseEntity<AssetsData> updateAssetsData(AssetsData assetsData, String id);
}