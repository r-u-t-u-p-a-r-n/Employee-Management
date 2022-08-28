package site.operations.Models;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="assets")
public class AssetsData
{
	@Id
	private String assetId ;
	
	@Column(nullable=false)
	private String name ;

	@Column(nullable=false)
	private String type ;

	@Column(nullable=false)
	private String price ;

	@Column(nullable=false)
	private String organizationId ;
}