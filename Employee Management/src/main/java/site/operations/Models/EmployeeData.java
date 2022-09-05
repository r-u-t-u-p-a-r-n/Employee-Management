package site.operations.Models;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="employees")
public class EmployeeData
{
	@Id
	private String employeeId ;
	
	@Column(nullable=false)
	private String name ;

	@Column(nullable=false)
	private String department ;

	@Column(nullable=false)
	private String position ;

	@Column(nullable=false)
	private String age ;

	@Column(nullable=false)
	private String joined ;

	@Column(nullable=false)
	private String salary ;

	@Column(nullable=false)
	private String status ;

	@Column(nullable=false)
	private String role ;

	@ManyToOne
	private OrgData organizationDetails ;
}