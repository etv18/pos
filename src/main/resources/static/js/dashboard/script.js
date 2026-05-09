const monthlySalesTotalCanvas = document.getElementById("monthlySalesTotalCanvas");
const monthlySalesTotalInstance = null;
const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

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
    const response = await fetch(`/api/v1/stats/monthly/sales/totals/${currentYear}`);
    const data = await response.json();
    console.log(data)
    const refactoredData = data.map(d => d.total);
    generateMonthlySalesTotalChart(
        monthlySalesTotalInstance,
        monthlySalesTotalCanvas,
        "bar",
        refactoredData
    )
});