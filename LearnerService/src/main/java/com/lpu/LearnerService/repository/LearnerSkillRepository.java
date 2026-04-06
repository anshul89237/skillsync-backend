package com.lpu.LearnerService.repository;
 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lpu.LearnerService.entity.LearnerSkill;
 
@Repository
public interface LearnerSkillRepository extends JpaRepository<LearnerSkill, Long> {
    List<LearnerSkill> findByUserId(Long userId);
    void deleteByUserIdAndSkillId(Long userId, Long skillId);
}
