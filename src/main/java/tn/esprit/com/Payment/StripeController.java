package tn.esprit.com.Payment;

import com.stripe.Stripe;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class StripeController {
    String stripeApiKey = "sk_test_51Omk7aIZrZQYiX0LL0kvcsWt4pdDdgwrmJLYViLhaqvGDxGx3aTKWnAQEnxRuO6IAdNZxPoOMvJKuCGavp2BDpV800MC00Y12O";
    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody Map<String, Object> requestBodyMap) throws Exception {

        Stripe.apiKey = "sk_test_51Omk7aIZrZQYiX0LL0kvcsWt4pdDdgwrmJLYViLhaqvGDxGx3aTKWnAQEnxRuO6IAdNZxPoOMvJKuCGavp2BDpV800MC00Y12O";

        Double totalCost = Double.parseDouble(requestBodyMap.get("totalCost").toString());
        Long quantity = Long.parseLong(requestBodyMap.get("quantity").toString());

        Long totalCostCents = (long) (totalCost * 100); // Converting total cost to cents

        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency("usd")
                .setUnitAmount(totalCostCents)
                .setProduct("prod_PeUN9Oe2d7fHzh") // Replace prod_123456789 with your actual product ID
                .build();

        Price price = Price.create(priceParams);
        String priceId = price.getId();

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(quantity)
                                .setPrice(priceId)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT) // Specify the mode as 'payment'
                .setSuccessUrl("https://example.com/success?session_id={CHECKOUT_SESSION_ID}") // Specify the success URL
                .setCancelUrl("https://example.com/cancel")
                .build();

        Session session = Session.create(params);
        String url = session.getUrl();

        System.out.println(session);
        return url;
    }}