# Check if adb is available
$adb = "adb"
$date = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
$outputFile = "app_recording_$date.mp4"

Write-Host "Android App Recording Script" -ForegroundColor Green
Write-Host "=========================" -ForegroundColor Green
Write-Host ""
Write-Host "This script will record your app screen." -ForegroundColor Cyan
Write-Host "Press Ctrl+C when you want to stop recording." -ForegroundColor Cyan
Write-Host ""
Write-Host "Starting recording..." -ForegroundColor Yellow

try {
    # Start the recording
    & $adb shell screenrecord /sdcard/$outputFile

    # Note: The script will wait here until Ctrl+C is pressed
} catch {
    Write-Host "`nRecording stopped." -ForegroundColor Yellow
} finally {
    # Pull the recording from the device
    Write-Host "Transferring recording from device..." -ForegroundColor Yellow
    & $adb pull /sdcard/$outputFile .

    # Delete the file from the device
    & $adb shell rm /sdcard/$outputFile

    if (Test-Path $outputFile) {
        Write-Host "`nRecording saved as: $outputFile" -ForegroundColor Green
        Write-Host "Location: $(Get-Location)\$outputFile" -ForegroundColor Green
    } else {
        Write-Host "`nError: Recording file not found" -ForegroundColor Red
    }
} 