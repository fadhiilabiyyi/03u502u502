package com.fadhiilabiyyi.license3j;

import jakarta.annotation.PostConstruct;
import javax0.license3j.License;
import javax0.license3j.io.LicenseReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;

@Service
@Slf4j
@EnableScheduling
public class LicenseValidator {
    private final TaskScheduler taskScheduler;
    private static boolean licenseExpired = false;

    public LicenseValidator(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @PostConstruct
    public void validateLicense() {
        try {
            log.info("[LicenseService] - Checking License");

            License license = new License();
            this.loadLicense(license);

            String macAddress = license.get("macAddress").getString();
            Date expireDate = license.get("expireDate").getDate();

            log.info("[LicenseService] - Mac Address {}", getPreferredMacAddress());
            log.info("[LicenseService] - Expire Date {}", expireDate);

            validateMac(macAddress);
            validateDate(expireDate);

            log.info("[LicenseService] - License is Valid");
        } catch (Exception e) {
            log.info("", e);
            log.error("[LicenseService] - License validation failed!");
        }
    }

    public String getPreferredMacAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            if (ni.isLoopback() || !ni.isUp() || ni.getHardwareAddress() == null) continue;

            String name = ni.getName().toLowerCase();
            String displayName = ni.getDisplayName().toLowerCase();

            // Prioritize common interface names
            if (name.startsWith("en") || name.startsWith("eth") || name.startsWith("wlan") ||
                    displayName.contains("ethernet") || displayName.contains("wi-fi")) {

                byte[] mac = ni.getHardwareAddress();
                String[] hex = new String[mac.length];
                for (int i = 0; i < mac.length; i++) {
                    hex[i] = String.format("%02x", mac[i]);
                }
                return String.join(":", hex);
            }
        }

        return "MAC address not found";
    }

    public boolean licenseExpired() {
        return licenseExpired;
    }

    public void licenseExpiredNotification(Date date) {
        Instant expirationInstant = date.toInstant().minusMillis(604_800_000);
        if (expirationInstant != null) {
            // TODO : Send Email
            taskScheduler.schedule(this::sendEmailToUser, expirationInstant);
        }
    }

    public void scheduledLicenseCheck(Date date) {
        Instant expirationInstant = date.toInstant();
        if (expirationInstant != null) {
            taskScheduler.schedule(this::validateLicense, expirationInstant);
            log.info("[LicenseService] - License check scheduled for: " + expirationInstant);
        }
    }

    public void sendEmailToUser() {

    }

    private void loadLicense(License license) throws IOException {
        byte[] licenseBytes = Files.readAllBytes(Paths.get("license.bin"));
        new LicenseReader(new ByteArrayInputStream(licenseBytes)).read();

        // Load License
        if (!license.isOK(LicenseKeyHolder.key)) {
            // Stop Application
            log.warn("[LicenseService] - License is not valid!");
            licenseExpired = true;
        }
    }

    private void validateMac(String macAddress) throws SocketException {
        if (!macAddress.equals(this.getPreferredMacAddress())) {
            log.warn("[LicenseService] - Mac Address did not match");
            licenseExpired = true;
        }
    }

    private void validateDate(Date expiredDate) {
        if (new Date().after(expiredDate)) {
            log.warn("[LicenseService] - License is expired");
            licenseExpired = true;
        } else {
            licenseExpiredNotification(expiredDate);
            scheduledLicenseCheck(expiredDate);
        }
    }
}
