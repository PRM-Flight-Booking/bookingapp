package com.example.bookingapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.data.model.Hotel;
import com.example.bookingapp.data.model.Place;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.data.repository.BillRepository;
import com.example.bookingapp.data.repository.FlightBillRepository;
import com.example.bookingapp.data.repository.FlightRepository;
import com.example.bookingapp.data.repository.HotelBillRepository;
import com.example.bookingapp.data.repository.HotelRepository;
import com.example.bookingapp.data.repository.PlaceBillRepository;
import com.example.bookingapp.data.repository.PlaceRepository;
import com.example.bookingapp.data.repository.UserRepository;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Tool {
    public static double calculateDistance(String location1, String location2){
        // Parse latitude and longitude from input strings
        String[] loc1 = location1.split(",");
        String[] loc2 = location2.split(",");

        double lat1 = Double.parseDouble(loc1[0].trim());
        double lon1 = Double.parseDouble(loc1[1].trim());
        double lat2 = Double.parseDouble(loc2[0].trim());
        double lon2 = Double.parseDouble(loc2[1].trim());

        // Radius of the Earth in kilometers
        final double R = 6371.0;

        // Convert degrees to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // Haversine formula
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Compute the distance
        return R * c; // Distance in kilometers
    }

    public static LatLng parseLocation(String location) {
        try {
            String[] parts = location.split(",");
            if (parts.length == 2) {
                double latitude = Double.parseDouble(parts[0].trim());
                double longitude = Double.parseDouble(parts[1].trim());
                return new LatLng(latitude, longitude);
            }
        } catch (Exception e) {
            Log.e("DetailPlaceFragment", "Error parsing location: " + e.getMessage());
        }
        return null;
    }
    @SuppressLint("DefaultLocale")
    public static String calculateFlightDuration(Date departureTime, Date arrivalTime) {
        if (departureTime == null || arrivalTime == null) {
            return "N/A"; // Return "N/A" for invalid input
        }

        // Ensure arrival time is after departure time
        if (arrivalTime.before(departureTime)) {
            return "Invalid Duration"; // Handle cases where arrival is before departure
        }

        // Calculate difference in milliseconds
        long diffInMillies = arrivalTime.getTime() - departureTime.getTime();

        // Convert to hours and minutes
        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillies);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillies) % 60;

        // Format the result
        return String.format("%dh %dm", hours, minutes);
    }


    public static void insertSampleData(Context context) {
        UserRepository userRepository = new UserRepository(context);
        FlightRepository flightRepository = new FlightRepository(context);
        HotelRepository hotelRepository = new HotelRepository(context);
        PlaceRepository placeRepository = new PlaceRepository(context);

        FlightBillRepository flightBillRepository = new FlightBillRepository(context);
        HotelBillRepository hotelBillRepository = new HotelBillRepository(context);
        PlaceBillRepository placeBillRepository = new PlaceBillRepository(context);
        BillRepository billRepository = new BillRepository(context);

        // Generate realistic dates
        Date departureDate = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000); // 2 days ahead
        Date arrivalDate = new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000); // 6 days ahead
        Date billDate = new Date(System.currentTimeMillis()); //now

//        // Insert Users
        userRepository.createUser(new User("lqt@gmail.com", "21.035392691485605, 105.83593244866566", "Alice", "123456"));

        User admin = new User("admin@gmail.com", "48.85569818520167, 2.2928476686721315", "Admin", "admin");
        admin.setAdmin(true);
        userRepository.createUser(admin);

        // Insert Hotels
        hotelRepository.createHotel(new Hotel(
                Collections.singletonList("https://www.ahstatic.com/photos/c096_ho_00_p_1024x768.jpg"),
                "48.85618462451042, 2.2926456269690836",
                "Pullman Paris Tour Eiffel", 200.0f, 4.5f,"Hotel Pullman Paris Tour Eiffel vista habitación"));

        hotelRepository.createHotel(new Hotel(
                Collections.singletonList("https://vanangroup.com.vn/wp-content/uploads/2024/10/29df21cd740c64fda44d8e567685970b-e1729733600172.jpg"),
                "40.76263568095324, -73.97772363953301",
                "The Manhattan at Times Square Hotel", 250.0f, 4.2f,"Beauty and relax"));

//         Insert Places
//         Insert Places with multiple images
        placeRepository.createPlace(new Place(
                "Eiffel Tower",
                "The Eiffel Tower is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889",
                "48.85838418882046, 2.2944491111660015",
                25.0f,
                Arrays.asList(
                        "https://i.postimg.cc/52xdFFbJ/c6.jpg",
                        "https://cmkt-image-prd.freetls.fastly.net/0.1.0/ps/3599736/1820/2675/m1/fpnw/wm1/vdk5qp6xnqj6yqrzkupcpss6gw0e59mwtz2vlofi7bsgzhf5wl2wfvzdnakywyde-.jpg?1511086930=&s=0a92052b6234e98205f0ab5be16ea6b6",
                        "https://images.pexels.com/photos/587844/pexels-photo-587844.jpeg?cs=srgb&dl=eiffel-tower-france-paris-587844.jpg&fm=jpg",
                        "https://www.sumit4allphotography.com/wp-content/uploads/2015/04/paris-013.jpg",
                        "https://2.bp.blogspot.com/-dn0TXkXVdoY/VNchaXoubBI/AAAAAAAAE7o/Fi_1MAVlLf0/s1600/20120827-DSC_2409.jpg"
                ),
                4.5f, 4
        ));
//
        placeRepository.createPlace(new Place(
                "Museum of Modern Art",
                "Works from Van Gogh to Warhol, a garden with many statues, 2 cafes and a Modern restaurant.",
                "40.77246750424053, -73.97402309481457",
                25.0f,
                Arrays.asList(
                        "https://i.postimg.cc/d34X25qK/Museum-of-Modern-Art-in-Warsaw-01-B.jpg",
                        "https://media.cntraveler.com/photos/5dae0325b45cd80008161cf3/16%3A9/w_2560%2Cc_limit/MOMA-2019_IVRPHOTO-4373_ArtistChoice_HB_007-2-2000x1125.jpg",
                        "https://www.alphacityguides.com/sites/default/files/moma1.jpg",
                        "https://media.architecturaldigest.com/photos/5d9f837526455c000815e398/4%3A3/w_2719%2Ch_2039%2Cc_limit/01_MoMA_Photography%20by%20Brett%20Beyer.jpg",
                        "https://www.grouptours.net/wp-content/uploads/2016/07/The_Museum_of_Modern_Arts_New_York_5907606980.jpg",
                        "https://media.cntraveler.com/photos/5c8682ccd3d43f7c5196ec96/16%3A9/w_2560%2Cc_limit/The-National-Museum-of-Modern-Art%2C-Tokyo_1.-The-exterior-of-The-National-Art-Center%2C-Tokyo.jpg",
                        "https://design-milk.com/images/2018/02/Museums-1-MOCAPE-Coop-Himmelblau.jpg",
                        "https://media.timeout.com/images/100614413/image.jpg"
                ),
                4.0f, 2
        ));

        placeRepository.createPlace(new Place(
                "Sapa",
                "Sa Pa is a district-level town of Lào Cai Province in the Northwest region of Vietnam. The town has an area of 685 km² and a population of 70,663 in 2022. The town capital lies at Sa Pa ward.",
                "22.36015195227598, 103.8267228946122",
                25.0f,
                Arrays.asList(
                        "https://i.postimg.cc/d0w2Fw24/lao-chai-y-linh-ho-village-900x473.webp",
                        "https://aavietnamtravel.com/wp-content/uploads/2019/05/wetrek-vn-sapa.jpg",
                        "https://trekkingtoursapa.com/wp-content/uploads/2023/03/8-1.webp",
                        "https://impresstravel.com/wp-content/uploads/2021/03/Sapa-Rice-Terraces-1.jpg",
                        "https://static-images.vnncdn.net/files/publish/2023/5/26/sapa-valley-feature-1492-684.jpg",
                        "https://pyt-blogs.imgix.net/2020/07/sapa-3553138_1920.jpg?auto=format&ixlib=php-3.3.0",
                        "https://divui.com/blog/wp-content/uploads/2018/10/sapa.jpg",
                        "https://ik.imagekit.io/tvlk/blog/2022/01/dia-diem-du-lich-sapa-27.jpg",
                        "https://media.techcity.cloud/vietnam.vn/2023/08/6162074_sapa_2.jpg"
                ),
                4.2f, 1
        ));

        flightRepository.createFlight(new Flight(
                arrivalDate,
                "21.219172757731393, 105.8036285673261",
                departureDate,
                "49.00902635788525, 2.5514694666655657",
                "Vietnam Airline",
                100
        ));
        flightRepository.createFlight(new Flight(
                arrivalDate,
                "21.219172757731393, 105.8036285673261",
                departureDate,
                "40.79209055199359, -73.87850528419962",
                "LaGuardia Airline",
                200
        ));

//        flightBillRepository.createFlightBill(new FlightBill(
//                1,               // idFlight (liên kết với Flight vừa tạo)
//                450.0f,          // price
//                6868,           // ticketNumber
//                1                // idUser (liên kết với User "Alice")
//        ));

//        // Insert Flight Bills
//        flightBillRepository.createFlightBill(new FlightBill(arrivalDate, departureDate, 1, 450.0f, 2,1));
//
//        // Insert Hotel Bills
//        hotelBillRepository.createHotelBill(new HotelBill(departureDate, 200.0f, "Deluxe", 3, 1,1));
//
//        // Insert Place Bills
//        placeBillRepository.createPlaceBill(new PlaceBill(25.0f, 3, 1,1));
//
//        // Insert Bills
//        billRepository.createBill(new Bill(billDate, "1", "1", "1", 450.0f * 2 + 200.0f * 3 + 25.0f * 3, 1));
    }
    public static String convertToAddress(Context context,String location) {
        if (location == null || !location.contains(",")) {
            return "Invalid Location";
        }
        try {
            // Split location string into latitude and longitude
            String[] parts = location.split(",");
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());

            // Use Geocoder to fetch address
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Get city & country
                String city = address.getLocality();   // City name (e.g., New York)
                String country = address.getCountryCode();  // Country name (e.g., USA)

                // Return formatted location
                if (city != null && country != null) {
                    return city + ", " + country; // Example: "New York, USA"
                } else if (country != null) {
                    return country; // If city is missing, return only country
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return "Unknown Location";
    }

}
