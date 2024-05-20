package org.luxeloot.backend.dataloaders;

import org.luxeloot.backend.models.Auction;
import org.luxeloot.backend.models.Lot;
import org.luxeloot.backend.models.User;
import org.luxeloot.backend.repository.AuctionRepository;
import org.luxeloot.backend.repository.LotsRepository;
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
@Order(3)
@PropertySource("classpath:application.yaml")
public class LotsDataLoader implements CommandLineRunner {

    @Autowired
    private LotsRepository lotRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    // Fixed seed for consistency in random generation
    private static final Random random = new Random(1234567L); // Using a fixed seed

    @Override
    public void run(String... args) throws Exception {
        Boolean seedData = Boolean.valueOf(env.getProperty("data.seed-data"));
        if (seedData) {
            loadLotData();
        } else {
            System.out.println("Seed-data param is set to false, LotsDataLoader will not run.");
        }
    }

    private void loadLotData() {

        for (int i = 0; i < 100; i++) {
            Auction rndAuction = randomAuction();
            Lot lot = Lot.builder()
                    .auction(rndAuction)
                    .lotOwner(randomOwner())
                    .logo(rndAuction.getLogo())
                    .status(randomStatus())
                    .name("Лот " + i)
                    .title("Моя ставка № " + i)
                    .description("Ставлю на покупку картин " + i)
                    .timer(random.nextInt(120) + 1) // Timer between 1 to 120 minutes
                    .startingPrice(rndAuction.getStartingPrice()) // Starting price between 0 and 1000
                    .bet(Math.round(rndAuction.getStartingPrice() + random.nextDouble() * 100)) // Bet increment between 0 and 100
                    .currentPrice(rndAuction.getStartingPrice() + Math.round(random.nextDouble() * 100)) // Current price between 0 and 1000
                    .endTime(LocalDateTime.now().plusDays(random.nextInt(30))) // End time between now and 30 days ahead
                    .build();

            lotRepository.save(lot);
        }
    }

    private String randomStatus() {
        String[] statuses = {"Участвует", "Закрыт", "На проверке", "Победитель"};
        return statuses[random.nextInt(statuses.length)];
    }

    private Auction randomAuction() {
        Long[] ids = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L, 24L};
        Long randomId = ids[random.nextInt(ids.length)];
        Auction auction = auctionRepository.findById(randomId).orElseThrow(() -> new RuntimeException("Auction not found"));
        return auction;
    }

    private User randomOwner() {
        Long[] ids = {2L, 3L};
        Long randomId = ids[random.nextInt(ids.length)];
        User user = userRepository.findById(randomId).orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }
}
