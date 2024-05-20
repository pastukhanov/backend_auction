package org.luxeloot.backend.dataloaders;

import org.luxeloot.backend.models.Auction;
import org.luxeloot.backend.repository.AuctionRepository;
import org.luxeloot.backend.models.User;
import org.luxeloot.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;


@Component
@Order(2)
@PropertySource("classpath:application.yaml")
public class AuctionDataLoader implements CommandLineRunner {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    private static final Random random = new Random(1234567L);

    @Override
    public void run(String... args) throws Exception {
        Boolean seedData = Boolean.valueOf(env.getProperty("data.seed-data"));
        if (seedData) {
            loadAuctionData();
        } else {
            System.out.println("Seed-data param is set to false, LotsDataLoader will not run.");
        }

    }

    private void loadAuctionData() {
        for (int i = 0; i < 24; i++) {

            LocalDateTime dt = LocalDateTime.now().plusDays(random.nextInt(14)).minusDays(random.nextInt(14));
            String status = dt.isBefore(LocalDateTime.now()) ? "Завершился" : "Планируется";

            Auction auction = Auction.builder()
                    .title("Продажа картин " + (i + 1))
                    .logo("logo" + (i + 1) + ".jpeg")
                    .status(status)
                    .topic("Живопись")
                    .startingPrice(Math.round(random.nextDouble() * 1000))
                    .auctionStartTime(dt)
                    .user(randomUser())
                    .lotsTimer(300)
                    .authors("Ван Гог")
                    .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Temporibus perspiciatis...")
                    .build();
            auctionRepository.save(auction);
        }
    }

    private User randomUser() {
        Long[] ids = {2L, 3L};
        Long randomId = ids[random.nextInt(ids.length)];
        User user = userRepository.findById(randomId).orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }
}
