package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer= customerRepository2.findById(customerId).get();
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> driverList = driverRepository2.findAll();

		int driverIdWithMinimum = Integer.MAX_VALUE;
		for(Driver driver : driverList){
			if(driver.getDriverId()<driverIdWithMinimum && driver.getCab().getAvailable()){
				driverIdWithMinimum = driver.getDriverId();
			}
		}
		if(driverIdWithMinimum==Integer.MAX_VALUE){
            throw new Exception();
		}

		Optional<Driver> optionalDriver =driverRepository2.findById(driverIdWithMinimum);
		if(!optionalDriver.isPresent()){
			throw new Exception();
		}
		Driver driver = optionalDriver.get();
		Cab cab=driver.getCab();

		Optional<Customer> optionalCustomer =customerRepository2.findById(customerId);
		if(!optionalCustomer.isPresent()){
			throw new Exception();
		}
		Customer customer = optionalCustomer.get();

		int billPrice=cab.getPerKmRate()*distanceInKm;

		TripBooking tripBooking =new TripBooking();
		       tripBooking.setDistanceInKm(distanceInKm);
			   tripBooking.setFromLocation(fromLocation);
			   tripBooking.setToLocation(toLocation);
			   tripBooking.setStatus(TripStatus.CONFIRMED);
			   tripBooking.setDriver(driver);
			   tripBooking.setCustomer(customer);
			   tripBooking.setBill(billPrice);

		cab.setAvailable(false);

		customer.getTripBookingList().add(tripBooking);
		driver.getTripBookingList().add(tripBooking);

		tripBookingRepository2.save(tripBooking);
		return tripBooking;

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.getDriver().getCab().setAvailable(true);
		List<TripBooking> tripBookingListOfDriver=tripBooking.getDriver().getTripBookingList();
		tripBookingListOfDriver.remove(tripBooking);

		List<TripBooking> tripBookingListOfCustomer=tripBooking.getCustomer().getTripBookingList();
		tripBookingListOfCustomer.remove(tripBooking);

		tripBooking.getDriver().setTripBookingList(tripBookingListOfDriver);
		tripBooking.getCustomer().setTripBookingList(tripBookingListOfCustomer);

		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
         TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
         tripBooking.setStatus(TripStatus.COMPLETED);
		 tripBooking.getDriver().getCab().setAvailable(true);

		List<TripBooking> tripBookingListOfDriver=tripBooking.getDriver().getTripBookingList();
		tripBookingListOfDriver.remove(tripBooking);

		List<TripBooking> tripBookingListOfCustomer=tripBooking.getCustomer().getTripBookingList();
		tripBookingListOfCustomer.remove(tripBooking);

		tripBookingListOfDriver.add(tripBooking);
		tripBookingListOfCustomer.add(tripBooking);

		tripBooking.getDriver().setTripBookingList(tripBookingListOfDriver);
		tripBooking.getCustomer().setTripBookingList(tripBookingListOfCustomer);

		tripBookingRepository2.save(tripBooking);

	}
}
