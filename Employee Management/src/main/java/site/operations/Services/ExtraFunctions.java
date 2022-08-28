package site.operations.Services;

import site.operations.Models.*;
import site.operations.Repositories.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

public interface ExtraFunctions
{
	String columnNames[] = {"Name","Department","Position",
                            "Age","Joined","Salary","Status","role"} ;

    static Set<String> getValues(String id, char c)
    {
        Set<String> S = new HashSet<>();
        String H ;
        int i = 0 ;
        while(i<id.length())
        {
            H = "" ;
            while(i<id.length() && id.charAt(i)!=c)
            {
                H+=id.charAt(i++);
            }
            i++ ;
            if(!(H.equals("")))
                S.add(H);
        }
        return S ;
    }

    static boolean checkCase(String S1, String S2)
    {
        int i = 0 ;
        if(S1==null || S2 == null)
            return false ;
        if(S1.length()!=S2.length())
            return false ;
        while(i<S1.length())
        {
             if(!((S1.charAt(i++)+"").equals((S2.charAt(i-1)+"").toUpperCase()) ||
                (S1.charAt(i-1)+"").equals((S2.charAt(i-1)+"").toLowerCase())))
                    return false ;
        }
        return true ;
    }

    static List<EmployeeData> getPropertyData(List<EmployeeData> L, String property)
    {
        String[] setValues = new String[columnNames.length];
        Set<String> propertyValues = getValues(property,',');
        int j ;
        for(EmployeeData ED : L)
        {
            j = 0 ;
            while(j<setValues.length)
            {
                setValues[j++] = null ;
            }
            for(String pr : propertyValues)
            {
                j = 0 ;
                for(String cn : columnNames)
                {
                    if(ExtraFunctions.checkCase(pr,cn))
                    {
                        if(j==0)
                            setValues[j] = ED.getName();
                        if(j==1)
                            setValues[j] = ED.getDepartment();
                        if(j==2)
                            setValues[j] = ED.getPosition();
                        if(j==3)
                            setValues[j] = ED.getAge();
                        if(j==4)
                            setValues[j] = ED.getJoined();
                        if(j==5)
                            setValues[j] = ED.getSalary();
                        if(j==6)
                            setValues[j] = ED.getStatus();
                        if(j==7)
                            setValues[j] = ED.getRole();
                    }
                    j++ ;
                }
            }

            ED.setName(setValues[0]); ED.setDepartment(setValues[1]);
            ED.setPosition(setValues[2]);
            ED.setAge(setValues[3]); ED.setJoined(setValues[4]);
            ED.setSalary(setValues[5]); ED.setStatus(setValues[6]);
            ED.setRole(setValues[7]);
        }
        return L ;
    }

    static AssetsData checkAssetsValidity(AssetsData assetsData, AssetsRepo assetsRepo)
    {
    	boolean b = true ;

    /*---------------------------------------------------------------------------*/

    	if(assetsData.getAssetId() == null || assetsData.getAssetId().length() == 0)
    		{ assetsData.setAssetId("Asset Id can\'t be null"); b = false ; }

    /*---------------------------------------------------------------------------*/

    	if(assetsData.getName()==null || assetsData.getName().length()==0)
            { assetsData.setName("Name can\'t be null"); b = false ; }

    /*---------------------------------------------------------------------------*/

        if(assetsData.getType() != null && checkCase(assetsData.getType(),"Electronic"))
        	assetsData.setType("Electronic");

        else if(assetsData.getType() != null && checkCase(assetsData.getType(),"Non-Electronic"))
        	assetsData.setType("Non-Electronic");

        else
        	{ assetsData.setType("Type must be Electronic or Non-Electronic"); b = false ; }

    /*---------------------------------------------------------------------------*/

    	if(assetsData.getPrice() == null || assetsData.getPrice().length() == 0)
          { assetsData.setPrice("Price can\'t be null"); b = false ; }
        
        else
        {
            try
        	{
        		int i = Integer.parseInt(assetsData.getPrice());
        		if(i<0)
                {
        			b = false ;
                    assetsData.setPrice("Price should be positive");
                }
        	}

        	catch(Exception e)
        	{
        		assetsData.setPrice("\'"+assetsData.getPrice()+"\' is not a number");
                b = false ;
        	}
        }

    /*---------------------------------------------------------------------------*/

        if(assetsData.getName() == null || assetsData.getName().length() == 0)
        {
            assetsData.setName("Assets can\'t be null"); b = false ;
        }

   	/*---------------------------------------------------------------------------*/

   		if(b)
            assetsData = assetsRepo.save(assetsData);
        return assetsData ;
    }

    static EmployeeData checkEmployeeValidity(EmployeeData employeeData, EmployeeRepo employeeRepo)
    {
        int i ; boolean b = true ;

    /*---------------------------------------------------------------------------*/

        if(employeeData.getEmployeeId()==null || employeeData.getEmployeeId().length()==0)
            { employeeData.setEmployeeId("Employee Id can\'t be null"); b = false ; }

    /*---------------------------------------------------------------------------*/

        if(employeeData.getName()==null || employeeData.getName().length()==0)
            { employeeData.setName("Name can\'t be null"); b = false ; }

    /*---------------------------------------------------------------------------*/
        
        if(employeeData.getDepartment() == null || employeeData.getDepartment().length()==0)
            { employeeData.setDepartment("Department can\'t be null") ;  b = false ; }

        else if(checkCase(employeeData.getDepartment(),"Software Development"))
            employeeData.setDepartment("Software Development");

        else if(checkCase(employeeData.getDepartment(),"Software Testing"))
            employeeData.setDepartment("Software Testing");

        else if(checkCase(employeeData.getDepartment(),"Finance Department"))
            employeeData.setDepartment("Finance Department");

        else
            { employeeData.setDepartment("Invalid Department"); b = false ; }

    /*---------------------------------------------------------------------------*/

        if(employeeData.getPosition() == null || employeeData.getPosition().length()==0)
            { employeeData.setPosition("Position can\'t be null"); b = false ; }

    /*---------------------------------------------------------------------------*/
        
        if(employeeData.getAge()==null || employeeData.getAge().length()==0)
            { employeeData.setAge("Age can\'t be null"); b = false ; }
        else
        {
            try
            {
                i = Integer.parseInt(employeeData.getAge());
                if(!(i>=21 && i<=60))
                    { employeeData.setAge("Age is not suitable"); b = false ; }
            }

            catch(Exception e)
            {
                employeeData.setAge("Age is invalid"); b = false ;
            }
        }

    /*---------------------------------------------------------------------------*/

        if(employeeData.getJoined()==null || employeeData.getJoined().length()==0)
            { employeeData.setJoined("Year can\'t be null");  b = false ; }
        else
        {
            try
            {
                i = Integer.parseInt(employeeData.getJoined());
                if(!(i>=2015 && i<=2022))
                    { employeeData.setJoined("Year is not suitable");  b = false ; }
            }

            catch(Exception e)
            {
                employeeData.setJoined("Year is invalid");  b = false ;
            }
        }   

    /*---------------------------------------------------------------------------*/

        if(employeeData.getSalary()==null || employeeData.getSalary().length()==0)
            { employeeData.setSalary("Salary can\'t be null"); b = false ; }
        else
        {
            try
            {
                i = Integer.parseInt(employeeData.getSalary());
                if(!(i>=1000 && i<=5000000))
                    { employeeData.setSalary("Salary is not suitable"); b = false ; }
            }

            catch(Exception e)
            {
                employeeData.setSalary("Salary is invalid"); b = false ;
            }
        }    

    /*---------------------------------------------------------------------------*/

        if(employeeData.getStatus() == null || employeeData.getStatus().length()==0)
            { employeeData.setStatus("Status can\'t be null") ; b = false ; }

        else if(checkCase(employeeData.getStatus(),"Active"))
            employeeData.setStatus("Active");

        else if(checkCase(employeeData.getStatus(),"Suspended"))
            employeeData.setStatus("Suspended");

        else if(checkCase(employeeData.getStatus(),"Terminated"))
            employeeData.setStatus("Terminated");

        else
            { employeeData.setStatus("Status is invalid"); b = false ; }

    /*---------------------------------------------------------------------------*/

        if(b)
            employeeData = employeeRepo.save(employeeData);
        return employeeData ;
    }

    /*---------------------------------------------------------------------------*/
}