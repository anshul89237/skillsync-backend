package com.lpu.SkillService.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lpu.SkillService.entity.Category;
 
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
