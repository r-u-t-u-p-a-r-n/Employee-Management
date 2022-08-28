package site.operations.Repositories;

import site.operations.Models.AssetsData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetsRepo extends JpaRepository<AssetsData,String> {}