
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

	@Query("select mr from MaintenanceRecord mr where mr.id = :masterId")
	MaintenanceRecord findMaintenanceRecordByMasterId(int masterId);

	@Query("select t from Task t where t.id = :taskId")
	Task findTaskByTaskId(int taskId);

	@Query("select t from Task t")
	Collection<Task> findAllDisponibleTasks();

}
