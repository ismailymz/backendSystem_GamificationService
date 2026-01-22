package gamification.adapter.out.persistence.jpaAdapterTest;

import de.thws.gamification.adapter.out.persistence.mapper.DriverProfileMapper;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction //All test methods execute transactional (separate, not all in one) -E.K
public class DriverProfileJpaTest {
    private static int counter = 0;
    private static void step(String msg){System.out.println("\n" +counter++ + "-)"+ "========== " + msg + " ==========");}
    private static void ok(String msg){System.out.println("✅ -------" + msg);}
    @Inject
    DriverProfileMapper mapper;
    @Inject
    DriverProfileRepository repo;


    //save, deleteById, findByUsername

     /*
     DriverProfile profile1 = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile1.setTotalPoints(25);

     DriverProfile profile2 = DriverProfile.createProfile("Cristiano Ronaldo", "siuuuuu");
        profile2.setTotalPoints(10);

     DriverProfile profile3 = DriverProfile.createProfile("Lionel Messi", "BalonDor");
        profile3.setTotalPoints(20);
     */
    //Testing to persist a DriverProfile and finding the profile from DataBase
    @Test

    void persistAndFindThePersistedProfilesById() throws Exception {
        DriverProfile profile1 = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile1.setTotalPoints(25);
        repo.save(profile1);
        DriverProfile profileFound;
        Optional<DriverProfile> profileFoundOpt = repo.findById(profile1.getId());
        if (profileFoundOpt.isPresent()) {
            profileFound = profileFoundOpt.get();
        } else {
            throw new AssertionError("Profile Not Found!");
        }

        assertEquals(profile1.getId(), profileFound.getId());
        assertEquals(profile1.getPassword(), profileFound.getPassword());
        assertEquals(profile1.getUsername(), profileFound.getUsername());

        DriverProfile profile2 = DriverProfile.createProfile("Cristiano Ronaldo", "siuuuuu");
        profile2.setTotalPoints(10);
        repo.save(profile2);
        DriverProfile profileFound2;
        Optional<DriverProfile> profileFoundOpt2 = repo.findById(profile2.getId());
        if (profileFoundOpt2.isPresent()) {
            profileFound2 = profileFoundOpt2.get();
        } else {
            throw new AssertionError("Profile Not Found!");
        }

        assertEquals(profile2.getId(), profileFound2.getId());
        assertEquals(profile2.getPassword(), profileFound2.getPassword());
        assertEquals(profile2.getUsername(), profileFound2.getUsername());

        DriverProfile profile3 = DriverProfile.createProfile("Lionel Messi", "BalonDor");
        profile3.setTotalPoints(20);
        repo.save(profile3);
        DriverProfile profileFound3;
        Optional<DriverProfile> profileFoundOpt3 = repo.findById(profile3.getId());
        if (profileFoundOpt3.isPresent()) {
            profileFound3 = profileFoundOpt3.get();
        } else {
            throw new AssertionError("Profile Not Found!");
        }

        assertEquals(profile3.getId(), profileFound3.getId());
        assertEquals(profile3.getPassword(), profileFound3.getPassword());
        assertEquals(profile3.getUsername(), profileFound3.getUsername());


        step("Three drivers have been created, persisted and then found successfully!");
    }
    @Test

    void listProfilesOrderByPointsDesc() {

        DriverProfile profile1 = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile1.setTotalPoints(25);
        repo.save(profile1);
        DriverProfile profile2 = DriverProfile.createProfile("Cristiano Ronaldo", "siuuuuu");
        profile2.setTotalPoints(10);
        repo.save(profile2);
        DriverProfile profile3 = DriverProfile.createProfile("Lionel Messi", "BalonDor");
        profile3.setTotalPoints(20);
        repo.save(profile3);


        List<DriverProfile> listOrderedByPoints = repo.findAllOrderByTotalPointsDesc();
        for (DriverProfile p : listOrderedByPoints) {
            System.out.println(p.toString());
        }
        step("Profiles have been listed!");

    }
    @Test

    void findByUsernameTwoDifferentProfiles() {
        DriverProfile profile = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile.setTotalPoints(25);
        repo.save(profile);
        DriverProfile profile2 = DriverProfile.createProfile("Cristiano Ronaldo", "siuuuuu");
        profile2.setTotalPoints(10);
        repo.save(profile2);

        Optional<DriverProfile> profileFoundByUsernameOpt = repo.findByUsername("Erdil");
        Optional<DriverProfile> profileFoundByUsernameOpt1 = repo.findByUsername("Cristiano Ronaldo");
        DriverProfile profileFoundByUsername;
        DriverProfile profileFoundByUsername1;

        if (profileFoundByUsernameOpt.isPresent()) {
            profileFoundByUsername = profileFoundByUsernameOpt.get();
        } else {
            throw new AssertionError("Profile Not Found!");
        }
        if (profileFoundByUsernameOpt1.isPresent()) {
            profileFoundByUsername1 = profileFoundByUsernameOpt1.get();
        } else {
            throw new AssertionError("Profile Not Found!");
        }

       assertEquals(profile.getId(), profileFoundByUsername.getId());
        assertEquals(profile.getPassword(), profileFoundByUsername.getPassword());
       assertEquals(profile.getUsername(), profileFoundByUsername.getUsername());
        assertEquals(profile2.getId(), profileFoundByUsername1.getId());
        assertEquals(profile2.getPassword(), profileFoundByUsername1.getPassword());
        assertEquals(profile2.getUsername(), profileFoundByUsername1.getUsername());
        step("Two different profiles have been found by their usernames successfully!");

    }

    @Test

    void removeProfileFromDataBase(){
        DriverProfile profile1 = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile1.setTotalPoints(25);
        repo.save(profile1);
        DriverProfile profile2 = DriverProfile.createProfile("Cristiano Ronaldo", "siuuuuu");
        profile2.setTotalPoints(10);
        repo.save(profile2);
        DriverProfile profile3 = DriverProfile.createProfile("Lionel Messi", "BalonDor");
        profile3.setTotalPoints(20);
        repo.save(profile3);

        Optional<DriverProfile> dpOpt= repo.findById(profile2.getId());
        if (dpOpt.isPresent()) System.out.println(dpOpt.get());

        repo.deleteById(profile2.getId());

        Optional<DriverProfile> dpOpt1 = repo.findById(profile2.getId());
        assertTrue(dpOpt1.isEmpty());
    }
@Test


    void searchProfilesWithFilter(){
        DriverProfile profile1 = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile1.setTotalPoints(25);
        repo.save(profile1);
        DriverProfile profile2 = DriverProfile.createProfile("Cristiano Ronaldo", "siuuuuu");
        profile2.setTotalPoints(10);
        repo.save(profile2);
        DriverProfile profile3 = DriverProfile.createProfile("Lionel Messi", "BalonDor");
        profile3.setTotalPoints(20);
        repo.save(profile3);
        DriverProfile profile4 = DriverProfile.createProfile("Erkan", "password1");
        profile4.setTotalPoints(30);
        repo.save(profile4);
        DriverProfile profile5 = DriverProfile.createProfile("Eray", "password2");
        profile5.setTotalPoints(15);
        repo.save(profile5);
        DriverProfile profile6 = DriverProfile.createProfile("Erhan", "password3");
        profile6.setTotalPoints(40);
        repo.save(profile6);
        DriverProfile profile7 = DriverProfile.createProfile("Eren", "password4");
        profile7.setTotalPoints(25);
        repo.save(profile7);
        DriverProfile profile8 = DriverProfile.createProfile("Neymar", "password5");
        profile8.setTotalPoints(5);
        repo.save(profile8);
        DriverProfile profile9 = DriverProfile.createProfile("Mbappe", "password6");
        profile9.setTotalPoints(35);
        repo.save(profile9);
        DriverProfile profile10 = DriverProfile.createProfile("Modric", "password7");
        profile10.setTotalPoints(15);
        repo.save(profile10);

    List<DriverProfile> expectedList1 = List.of(
                    profile1, profile4, profile5, profile6, profile7
            ).stream()
            .sorted(Comparator.comparingInt(DriverProfile::getTotalPoints).reversed())
            .toList();


        List<DriverProfile> driversNameStartWithEr=repo.searchDrivers("Er","",0);
        for(DriverProfile dp : driversNameStartWithEr){
            System.out.println(dp);
        }
    assertEquals(
            expectedList1.stream().map(DriverProfile::getId).toList(),
            driversNameStartWithEr.stream().map(DriverProfile::getId).toList()
    ); //There is no equals Override in the domain class. That's why I compared with id's. -E. K.
    }



@Test
    void tryingToPersistSameProfileTwice(){
        DriverProfile profile1 = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile1.setTotalPoints(25);
        repo.save(profile1);
        repo.save(profile1);
        //Checking if there is more than one identical entity in the database

        List<DriverProfile> allDrivers = repo.searchDrivers("", "", 0);
        assertEquals(1, allDrivers.size());
}



@Test

    void changeUsernameOfProfile(){
        DriverProfile profile1 = DriverProfile.createProfile("Erdil", "viergewinnt");
        profile1.setTotalPoints(25);
        String usernameBefore = profile1.getUsername();
        repo.save(profile1);
        DriverProfile profileAfter = new DriverProfile(profile1.getId(),"Alex", profile1.getPassword(),profile1.getRole(), profile1.getTotalPoints());
        profile1.setTotalPoints(25);
        repo.save(profileAfter);
        Optional<DriverProfile> profileOpt= repo.findById(profile1.getId());
        DriverProfile profileChanged = null;
        if(profileOpt.isPresent()) { profileChanged = profileOpt.get();}

        assertNotEquals(usernameBefore, profileChanged.getUsername() );
}
}
