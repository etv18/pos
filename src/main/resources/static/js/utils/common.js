export async function makeRequestToBackend(url, payload, method, errorMessage) {
    let data;

    try {
        const response = await fetch(url, {
            method: method.toUpperCase(),
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        data = await response.json();

        if (!response.ok) {
            const message = errorMessage ?? data.message;
            alert("ERROR: " + message);
            return;
        }

        return data;

    } catch (error) {
        console.error(error);
    }

}

export function inputIsEmpty(input){
    return !input.value || input.value.trim().length === 0
}

export function setupCurrencyInput(input) {

    input.addEventListener("input", function (event) {

        // Keep only numbers and dots
        let value = event.target.value.replace(/[^0-9.]/g, "");

        // Prevent multiple decimal points
        const parts = value.split(".");
        if (parts.length > 2) {
            value = parts[0] + "." + parts.slice(1).join("");
        }

        // Split integer and decimal parts
        let [integerPart, decimalPart] = value.split(".");

        // Remove leading zeros unless value is "0"
        integerPart = integerPart.replace(/^0+(?=\d)/, "");

        // Add commas to integer part
        integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

        // Rebuild value
        value = decimalPart !== undefined
            ? `${integerPart}.${decimalPart}`
            : integerPart;

        event.target.value = value;
    });
}
