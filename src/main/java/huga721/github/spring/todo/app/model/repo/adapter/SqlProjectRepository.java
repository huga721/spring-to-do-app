package huga721.github.spring.todo.app.model.repo.adapter;

import huga721.github.spring.todo.app.model.entities.Project;
import huga721.github.spring.todo.app.model.repo.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct g from Project g join fetch g.steps")
    List<Project> findAll();
}
