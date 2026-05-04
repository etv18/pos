export async function makeRequestToBackend(url, payload, method) {
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
            alert("ERROR: " + data.message);
            return;
        }

        return data;

    } catch (error) {
        console.error(error.message);
    }

}
