package site.operations.Repositories;

import site.operations.Models.OrgData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgRepo extends JpaRepository<OrgData,String> {}