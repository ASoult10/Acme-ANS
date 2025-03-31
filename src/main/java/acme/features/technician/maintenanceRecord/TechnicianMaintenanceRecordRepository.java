
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.mappings.InvolvedIn;
import acme.realms.Technician;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.technician.id =:technicianId")
	Collection<MaintenanceRecord> findAllMaintenanceRecordsByTechnician(Integer technicianId);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mr from MaintenanceRecord mr")
	Collection<MaintenanceRecord> findAllMaintenanceRecords();

	@Query("select t from Technician t")
	Collection<Technician> findAllTechnicians();

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select t from Technician t where t.id = :id")
	Technician findTechnicianById(int id);

	@Query("select ii from InvolvedIn ii where ii.maintenanceRecord.id = :maintenanceRecordId")
	Collection<InvolvedIn> findInvolvedInByMaintenanceRecordId(int maintenanceRecordId);
}
