//package dev.seshman.repository
//
//import dev.seshman.AbstractTest
//import dev.seshman.domain.Item
//import dev.seshman.domain.Session
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
//import org.springframework.data.domain.Pageable
//import java.time.LocalDate
//
///**
// * @author vladov 2019-03-16
// */
//@DataMongoTest
//class ItemRepositoryTest(
//        @Autowired private val itemRepository: ItemRepository,
//        @Autowired private val sessionRepository: SessionRepository) : AbstractTest() {
//
//    @BeforeEach
//    fun setup() {
//        val session = sessionRepository.save(Session(null ,LocalDate.now(), ""))
//        val item1 = itemRepository.save(Item(null, "desc", "sda", session.id))
//        val item2 = itemRepository.save(Item(null, "desc", "sda", session.id))
//        val savedSession = sessionRepository.findById(session.id!!).get()
////        savedSession.items.add(item1)
////        savedSession.items.add(item2)
//        sessionRepository.save(savedSession)
//    }
//
//    @Test
//    fun testFindAllSessionsSuccess() {
//        val sessions = sessionRepository.findAll(Pageable.unpaged())
//        print(sessions)
//    }
//
//    @Test
//    fun testGetAllItemsSuccess() {
//        val items = itemRepository.findAll(Pageable.unpaged())
////        print(items)
//    }
//}
