       Http Method                    Url                           Authority            
                                                                          
           GET           localhost:8080/admin/emp/{id}              Admin,Org            
           GET           localhost:8080/admin/ast/{id}              Admin,Org            
          POST           localhost:8080/admin/emp/add               Admin,Org            
          POST           localhost:8080/admin/ast/add               Admin,Org            
           PUT           localhost:8080/admin/emp/update/{id}       Admin,Org            
           PUT           localhost:8080/admin/ast/update/{id}       Admin,Org            
                                                                                         
        DELETE           localhost:8080/org/emp/delete/{id}         Org                  
        DELETE           localhost:8080/org/ast/delete/{id}         Org                  
        DELETE           localhost:8080/org/delete                  Org                  
          POST           localhost:8080/org/add                     ---                  
           PUT           localhost:8080/org/update                  Org                  
                                                                                         
           GET           localhost:8080/basic/employee/profile      Employee,Admin       
           GET           localhost:8080/basic/assets/{id}           Employee,Admin,Org   
           GET           localhost:8080/basic/org                   Employee,Admin,Org   
           GET           localhost:8080/basic/login                 Employee,Admin,Org   
           PUT           localhost:8080/basic/update                Employee,Admin,Org   
                                                                                         
