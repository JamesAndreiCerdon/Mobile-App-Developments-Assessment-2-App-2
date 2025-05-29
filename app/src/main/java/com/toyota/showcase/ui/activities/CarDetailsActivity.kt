package com.toyota.showcase.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.toyota.showcase.R
import com.toyota.showcase.model.ToyotaCar

class CarDetailsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_CAR = "extra_car"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val car = intent.getParcelableExtra<ToyotaCar>(EXTRA_CAR)
        car?.let { setupCarDetails(it) }
    }

    private fun setupCarDetails(car: ToyotaCar) {
        findViewById<ImageView>(R.id.carImage).setImageResource(car.image)
        findViewById<TextView>(R.id.carName).text = car.name
        findViewById<TextView>(R.id.carPrice).text = car.price
        findViewById<TextView>(R.id.carEngine).text = car.engine
        findViewById<TextView>(R.id.carTransmission).text = car.transmission
        findViewById<TextView>(R.id.carFuelEfficiency).text = car.fuelEfficiency
        findViewById<TextView>(R.id.carSeating).text = car.seating
        findViewById<TextView>(R.id.carCargo).text = car.cargo
        findViewById<TextView>(R.id.carSafety).text = car.safety
        findViewById<TextView>(R.id.carTopTrim).text = car.topTrim
        findViewById<TextView>(R.id.carLease).text = car.lease
        findViewById<TextView>(R.id.carDriveType).text = car.driveType
        findViewById<TextView>(R.id.carFuelTank).text = car.fuelTank
        findViewById<TextView>(R.id.carGroundClearance).text = car.groundClearance
        findViewById<TextView>(R.id.carInfotainment).text = car.infotainment
        findViewById<TextView>(R.id.carAudio).text = car.audio
        findViewById<TextView>(R.id.carWheels).text = car.wheels
        findViewById<TextView>(R.id.carSuspension).text = car.suspension
        findViewById<TextView>(R.id.carBrakes).text = car.brakes
        findViewById<TextView>(R.id.carDimensions).text = car.dimensions
        findViewById<TextView>(R.id.carWarranty).text = car.warranty
        findViewById<TextView>(R.id.carAcceleration).text = car.acceleration
        findViewById<TextView>(R.id.carTorque).text = car.torque
        findViewById<TextView>(R.id.carInterior).text = car.interior
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 