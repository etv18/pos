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

        if (!response.ok) {
            const errorData = await response.json();
            // Swal.fire({
            //     icon: 'error',
            //     title: 'Error',
            //     text: errorData.error || 'Something went wrong.'
            // });
            console.error(errorData);
            return;
        }

        data = await response.json();
        console.log(data);
        return data;

    } catch (error) {
        console.error(error.message);
    }

}
