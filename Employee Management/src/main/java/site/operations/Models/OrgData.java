package site.operations.Models;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="organization")
public class OrgData
{
	@Id
	private String orgId ;
	
	@Column(nullable=false)
	private String name ;
}