document.addEventListener("DOMContentLoaded", () => {
    fetchStats();
    fetchBloodStock();      // for admin.html
    loadBloodStockInAdmin(); // for adminBloodStock.html
});


function fetchStats() {
    fetch("/api/admin/dashboard/stats")
        .then(res => res.json())
        .then(data => {
            document.getElementById("totalDonors").textContent = data.totalDonors;
            document.getElementById("totalRequests").textContent = data.totalRequests;
            document.getElementById("approvedRequests").textContent = data.approvedRequests;
            document.getElementById("totalBloodUnits").textContent = data.totalBloodUnits;
        })
        .catch(err => console.error("Failed to load stats", err));
}

function fetchBloodStock() {
    fetch("/api/admin/blood-stock")
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("bloodStockContainer");
            container.innerHTML = "";

            const bloodGroups = [
                "A+", "B+", "O+", "AB+",
                "A-", "B-", "O-", "AB-"
            ];

            // Create a map from backend data
            const stockMap = {};
            data.forEach(item => {
                stockMap[item.bloodGroup.trim().toUpperCase()] = item.unit;
            });

            // Build UI for all 8 groups
            bloodGroups.forEach(group => {
                const units = stockMap[group] ?? 0; // if not found, default to 0

                const card = `
                    <div class="blood-card">
                        <div class="blood-type">${group}</div>
                        <div class="blood-icon"><i class="fas fa-tint"></i></div>
                        <div class="blood-units">${units}</div>
                    </div>
                `;
                container.innerHTML += card;
            });
        })
        .catch(err => console.error("Failed to load blood stock", err));
}
