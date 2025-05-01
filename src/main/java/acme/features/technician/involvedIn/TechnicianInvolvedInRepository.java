
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.mappings.InvolvedIn;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianInvolvedInRepository extends AbstractRepository {

	@Query("select ii from InvolvedIn ii where ii.maintenanceRecord.id = :maintenanceRecordId")
	Collection<InvolvedIn> findInvolvedInByMaintenanceRecordId(int maintenanceRecordId);

	@Query("select ii from InvolvedIn ii where ii.id = :id")
	InvolvedIn findInvolvedInById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :maintenanceRecordId")
	MaintenanceRecord findMaintenanceRecordById(int maintenanceRecordId);

	@Query("select tk from Task tk where tk.id = :id")
	Task findTaskById(int id);

	@Query("select tk from Task tk")
	Collection<Task> findAllDisponibleTasks();

	@Query("select tk from Task tk where tk.description not in (select ii.task.description from InvolvedIn ii where ii.maintenanceRecord.id = :maintenanceRecordId)")
	Collection<Task> findTasksNotInMaintenanceRecord(int maintenanceRecordId);

}
