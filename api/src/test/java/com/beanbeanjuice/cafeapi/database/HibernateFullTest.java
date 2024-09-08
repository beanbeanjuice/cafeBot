package com.beanbeanjuice.cafeapi.database;

import com.beanbeanjuice.cafeapi.endpoints.cafe.authentication.RoleEntity;
import com.beanbeanjuice.cafeapi.endpoints.cafe.user.CafeUserEntity;
import com.beanbeanjuice.cafeapi.service.DatabaseService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.List;

public class HibernateFullTest {

    private SessionFactory sessionFactory;

    @BeforeEach
    protected void setUp() throws Exception {
        sessionFactory = DatabaseService.getSessionFactory();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @Test
    @DisplayName("Add Test Role")
    public void addRole() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            session.beginTransaction();

            RoleEntity role = new RoleEntity();
            role.setRoleName("TEST");

            session.persist(role);

            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Fetch Roles")
    public void fetchRoles() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            session.beginTransaction();

            List<RoleEntity> roles = session.createQuery("SELECT role FROM RoleEntity role", RoleEntity.class).list();

            Assertions.assertTrue(roles.stream().map(RoleEntity::getRoleName).toList().contains("TEST"));

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Delete Test Role")
    public void deleteRole() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            {
                session.beginTransaction();

                List<RoleEntity> roles = session.createQuery("SELECT role FROM RoleEntity role", RoleEntity.class).list();
                roles.stream().filter((role) -> role.getRoleName().equals("TEST")).forEach(session::remove);

                session.getTransaction().commit();
            }

            {
                session.beginTransaction();

                List<RoleEntity> roles = session.createQuery("SELECT role FROM RoleEntity role", RoleEntity.class).list();

                Assertions.assertFalse(roles.stream().map(RoleEntity::getRoleName).toList().contains("TEST"));

                session.getTransaction().commit();
            }
        }
    }

    @Test
    @DisplayName("Create User")
    public void createUser() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            session.beginTransaction();

            CafeUserEntity user = new CafeUserEntity();
            user.setUsername("create_user_test");
            user.setEmail("create_user_test@test.com");
            user.setPassword("$2a$12$OBnerD3ZrnkqY/ofkaxune1jnpUscFhTGCcuVA9x5lgAGAtr6Bss2");
            user.setFirstName("create_user_test_first_name");

            session.persist(user);

            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Fetch Users")
    public void fetchUsers() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            session.beginTransaction();

            List<CafeUserEntity> users = session.createQuery("SELECT user FROM CafeUserEntity user", CafeUserEntity.class).list();

            users.forEach(System.out::println);

            session.getTransaction().commit();
        }
    }

//    @SuppressWarnings("unchecked")
//    @Test
//    public void testBasicUsage() {
//        UserEntity user = new UserEntity();
//        user.setId(5);
//        user.setUsername("johndoe_123");
//        user.setEmail("john@test.com");
//        user.setPassword("$2a$12$OBnerD3ZrnkqY/ofkaxune1jnpUscFhTGCcuVA9x5lgAGAtr6Bss2");
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setRoles(List.of("ROLE_USER"));
//
//        // create a couple of events...
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        session.remove(new UserEntity());
//        session.getTransaction().commit();
//        session.close();
//
//        session = sessionFactory.openSession();
//        session.beginTransaction();
//        List<UserEntity> result = session.createQuery( "select u from UserEntity u" , UserEntity.class).list();
//        for (UserEntity userEntity : result) {
//            System.out.println( "User (" + userEntity.getUsername() + ") : " + userEntity.getEmail() );
//        }
//        session.getTransaction().commit();
//        session.close();
//    }
//
//    @Test
//    public void marco_is_in_the_house() {
//        assertThat(1).isGreaterThanOrEqualTo(0);
//    }
}
