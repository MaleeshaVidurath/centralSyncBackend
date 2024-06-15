package CentralSync.demo.repository;
import CentralSync.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByUser(User user);

    @Query("SELECT t FROM Ticket t WHERE FUNCTION('YEAR', t.date) = :year")
    List<Ticket> ticketsByYear(@Param("year") int year);

    List<Ticket> findAllByItemId_ItemGroup(ItemGroupEnum itemGroup);
    @Query("SELECT t.itemId,COUNT(*) FROM Ticket t GROUP BY t.itemId ORDER BY COUNT(*) DESC LIMIT 1")
    Long findItemIdWithMaxCount(List<Ticket> ticketList);


}
