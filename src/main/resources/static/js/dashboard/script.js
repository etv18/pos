const monthlySalesTotalCanvas = document.getElementById("monthlySalesTotalCanvas");
const pAvailableItems = document.getElementById("pAvailableItems");
const pRunningLowItems = document.getElementById("pRunningLowItems");
const pOutOfStockItems = document.getElementById("pOutOfStockItems");
const pTotalSalesToday = document.getElementById("pTotalSalesToday");
const pSoldItems = document.getElementById("pSoldItems");

const monthlySalesTotalInstance = null;
const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
const monthlySalesTotalsEndpoint = "/api/v1/stats/monthly/sales/totals";
const dashboardDataEndpoint = "/api/v1/stats/dashboard/data";

async function fetchBackendData(url) {
    const response = await fetch(url);
    const data = await response.json();
    if(!response.ok) {
        console.error(data.message);
        return null;
    }
    return data;
}

async function setDataValuesOnDashboard(){
    const data = await fetchBackendData(dashboardDataEndpoint);
    console.log(data);
    pAvailableItems.textContent  = data.availableItems;
    pRunningLowItems.textContent = data.runningLowItems;
    pOutOfStockItems.textContent = data.outOfStockItems;
    pTotalSalesToday.textContent = toCurrency(data.totalTodaySales);
    pSoldItems.textContent       = data.soldItems;
}

function toCurrency(numberInStr){
    const formatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });
    return formatter.format(numberInStr);
}

function generateMonthlySalesTotalChart(chartInstance, canvas, type, data){
    if(chartInstance) chartInstance.destroy();
    chartInstance = new Chart(canvas, {
        type: type,
        data: {
            labels: months,
            datasets: [
                {
                    label: "Sales",
                    data: data,
                    backgroundColor: "rgb(14 239 115)",
                    borderColor: "rgb(29 93 30)",
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

document.addEventListener("DOMContentLoaded", async e =>{
    const currentYear = new Date().getFullYear();
    const data = await fetchBackendData(`${monthlySalesTotalsEndpoint}/${currentYear}`);
    console.log(data)
    const refactoredData = data.map(d => d.total);
    generateMonthlySalesTotalChart(
        monthlySalesTotalInstance,
        monthlySalesTotalCanvas,
        "bar",
        refactoredData
    )

    await setDataValuesOnDashboard();
});